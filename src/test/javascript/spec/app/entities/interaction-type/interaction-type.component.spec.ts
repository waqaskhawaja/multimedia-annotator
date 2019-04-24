/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MultimediaAnnotatorTestModule } from '../../../test.module';
import { InteractionTypeComponent } from 'app/entities/interaction-type/interaction-type.component';
import { InteractionTypeService } from 'app/entities/interaction-type/interaction-type.service';
import { InteractionType } from 'app/shared/model/interaction-type.model';

describe('Component Tests', () => {
    describe('InteractionType Management Component', () => {
        let comp: InteractionTypeComponent;
        let fixture: ComponentFixture<InteractionTypeComponent>;
        let service: InteractionTypeService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MultimediaAnnotatorTestModule],
                declarations: [InteractionTypeComponent],
                providers: []
            })
                .overrideTemplate(InteractionTypeComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(InteractionTypeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InteractionTypeService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new InteractionType(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.interactionTypes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
