import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { MaTestModule } from '../../../test.module';
import { DataSetResourceUpdateComponent } from 'app/entities/data-set-resource/data-set-resource-update.component';
import { DataSetResourceService } from 'app/entities/data-set-resource/data-set-resource.service';
import { DataSetResource } from 'app/shared/model/data-set-resource.model';

describe('Component Tests', () => {
    describe('DataSetResource Management Update Component', () => {
        let comp: DataSetResourceUpdateComponent;
        let fixture: ComponentFixture<DataSetResourceUpdateComponent>;
        let service: DataSetResourceService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MaTestModule],
                declarations: [DataSetResourceUpdateComponent],
                providers: [FormBuilder]
            })
                .overrideTemplate(DataSetResourceUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DataSetResourceUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DataSetResourceService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new DataSetResource(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.updateForm(entity);
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new DataSetResource();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.updateForm(entity);
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
