/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { MultimediaAnnotatorTestModule } from '../../../test.module';
import { SourceDataTypeUpdateComponent } from 'app/entities/source-data-type/source-data-type-update.component';
import { SourceDataTypeService } from 'app/entities/source-data-type/source-data-type.service';
import { SourceDataType } from 'app/shared/model/source-data-type.model';

describe('Component Tests', () => {
    describe('SourceDataType Management Update Component', () => {
        let comp: SourceDataTypeUpdateComponent;
        let fixture: ComponentFixture<SourceDataTypeUpdateComponent>;
        let service: SourceDataTypeService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MultimediaAnnotatorTestModule],
                declarations: [SourceDataTypeUpdateComponent]
            })
                .overrideTemplate(SourceDataTypeUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SourceDataTypeUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SourceDataTypeService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new SourceDataType(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.sourceDataType = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new SourceDataType();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.sourceDataType = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
